package hr.fer.tel.moovis.service;

import java.util.List;
import hr.fer.tel.moovis.dao.ApplicationUserDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.web.controllers.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacebookLikesUpdaterProcess implements Runnable {

	@Autowired
	private ApplicationUserDao appUserDao;
	@Autowired
	private FacebookProfileUpdater fbupdater;

	private volatile boolean isRunning = true;

	public FacebookLikesUpdaterProcess() {
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public void run() {
		long startTime = 0, endTime = 0;
		long maxDurationWithoutUpdate = 1000 * 60;
		System.out.println(Logger.getLogString(System.currentTimeMillis(),
				"FacebookLikesUpdaterProcess starts"));
		while (isRunning) {

			startTime = System.currentTimeMillis();
			updateUsers();
			endTime = System.currentTimeMillis();
			// System.out.println(Logger.getLogString(System.currentTimeMillis(),
			// "FacebookLikesUpdaterProcess ends"));
			// System.out.println(Logger.getLogString(System.currentTimeMillis(),
			// "FacebookLikesUpdaterProcess duration:"
			// + ((((double) endTime) - startTime) / 1000.))
			// + "s");
			if ((endTime - startTime) < maxDurationWithoutUpdate) {
				try {
					Thread.sleep(1000 * 20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void updateUsers() {
		List<ApplicationUser> users = appUserDao.findAll();

		for (ApplicationUser applicationUser : users) {
			fbupdater.updateUserLikes(applicationUser);
		}
	}

}
