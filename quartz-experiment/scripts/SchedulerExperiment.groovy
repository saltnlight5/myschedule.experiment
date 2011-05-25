import org.quartz.*
class SchedulerExperiment {
	Scheduler scheduler
	def init() {
		println("=" * 80)
		println("injected scheduler " + scheduler);
		println("=" * 80)
	}
}