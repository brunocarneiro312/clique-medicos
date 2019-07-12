package com.hepta.cliquemedicos.job;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.hepta.cliquemedicos.util.AmbienteUtil;

/**
 * Scheduler configurado como ServletContextListener para não precisar do
 * jobs.xml
 * 
 * @author gustavo.oliveira
 *
 */
public final class QuartzJobScheduler implements ServletContextListener {

	private QuartzJobScheduler classe;
	private Trigger triggerPagSeguro;
	private JobDetail jobPagSeguro;
	
	private Trigger triggerBoleto;
	private JobDetail jobBoleto;
	
	@SuppressWarnings("unused")
	private ServletContext context = null;

	public void execute() throws Exception {
		jobPagSeguro = JobBuilder.newJob(PagSeguroJob.class).withIdentity("pagSeguroJob", "group1").build();
		triggerPagSeguro = TriggerBuilder.newTrigger().withIdentity("pagSeguroJob", "group1")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(AmbienteUtil.TEMPO_JOB_PAGSEGURO).repeatForever())
				.build();
		runJob(jobPagSeguro, triggerPagSeguro);
		
		jobBoleto = JobBuilder.newJob(BoletoJob.class).withIdentity("boletoJob", "group2").build();
		triggerBoleto = TriggerBuilder.newTrigger().withIdentity("boletoJob", "group2")
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(AmbienteUtil.TEMPO_JOB_BOLETO).repeatForever())
				.build();
		runJob(jobBoleto, triggerBoleto);
	}
	
	private void runJob(JobDetail job, Trigger trigger) throws SchedulerException {
		// schedule it
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		Trigger test = scheduler.getTrigger(trigger.getKey());
		if (test == null) {
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		this.context = arg0.getServletContext();
		try {
			if (classe == null) {
				classe = new QuartzJobScheduler();
				classe.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
