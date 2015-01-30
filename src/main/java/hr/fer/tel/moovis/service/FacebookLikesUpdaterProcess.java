package hr.fer.tel.moovis.service;

import java.util.List;

import javax.annotation.PostConstruct;

import hr.fer.tel.moovis.dao.ApplicationUserDao;
import hr.fer.tel.moovis.model.ApplicationUser;
import hr.fer.tel.moovis.web.controllers.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

@Service
public class FacebookLikesUpdaterProcess implements Runnable,
		ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ApplicationUserDao appUserDao;
	@Autowired
	private FacebookProfileUpdater fbupdater;

	private volatile boolean isRunning = true;

	private boolean started = false;

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

		List<ApplicationUser> users = null;
		try {
			users = appUserDao.findAll();
		} catch (Exception e1) {
			System.err
					.println(Logger.getLogString(
							System.currentTimeMillis(),
							this.getClass().getCanonicalName()
									+ ": error while appUserDao.findAll. Exception message:"
									+ e1.getMessage()));
			return;
		}

		for (ApplicationUser applicationUser : users) {
			try {
				fbupdater.updateUserLikes(applicationUser);

			} catch (Exception e) {
				System.err.print(Logger.getLogString(
						System.currentTimeMillis(),
						"Error occurs while updating facebook movie likes for user:"
								+ applicationUser.getName() + " "
								+ applicationUser.getSurname()
								+ "\nException message:"));
				e.printStackTrace();
			}
		}
	}

	@PostConstruct
	public void processStart() {

	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		if (started) {
			return;
		}
		// pokreni faceupdater i daodaj mu shutdown hook
		final FacebookLikesUpdaterProcess fbUpdater = arg0
				.getApplicationContext().getBean(
						FacebookLikesUpdaterProcess.class);
		final Thread updaterThread = new Thread(fbUpdater);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				fbUpdater.setRunning(false);
				try {
					updaterThread.join();
					System.out.println("Fbupdater thread finished");
				} catch (InterruptedException e) {
					System.out.println("Fbupdater thread fail");
					e.printStackTrace();
				}
			}
		});
		updaterThread.start();
		started = true;
	}
}
