package net.d_bor.projects.minecraft.cratesystem.data;

import org.bukkit.Bukkit;

public abstract class SchedulerRunnable implements Runnable {

	private boolean stop = false;
	private int id = -1;
	private long counter;

	public SchedulerRunnable() {

		this(0);

	}

	public SchedulerRunnable(long counterStart) {

		this.counter = counterStart;

	}

	public abstract void task();

	@Override
	public void run() {

		task();

		counter++;

		if (stop) {

			Bukkit.getScheduler().cancelTask(id);

		}

	}

	protected long getCounter() {

		return counter;

	}

	protected void resetCounter() {

		counter = 0;

	}

	protected void stop() {

		stop = true;

	}

	public void setID(int id) {

		this.id = id;

	}
}
