package sit707_week7;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class BodyTemperatureMonitorTest {

	// Mocked TemperatureSensor
	TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
	// Mocked notificationSender
	NotificationSender notificationSender = Mockito.mock(NotificationSender.class);
	// Mocked cloudService
	CloudService cloudService = Mockito.mock(CloudService.class);

	@Test
	public void testStudentIdentity() {
		String studentId = "222207899";
		Assert.assertNotNull("Student ID is null", studentId);
	}

	@Test
	public void testStudentName() {
		String studentName = "Vishuddha Samarasekara";
		Assert.assertNotNull("Student name is null", studentName);
	}

	@Test
	public void testReadTemperatureNegative() {
		// dummy temperature reading
		double temperatureValue = -1.0;
		Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(temperatureValue);

		BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
		double actualTemperature = temperatureMonitor.readTemperature();
		Assert.assertEquals(temperatureValue, actualTemperature, 0.01);
	}

	@Test
	public void testReadTemperatureZero() {
		// dummy temperature reading
		double tempeValue = 0;
		Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(tempeValue);

		BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
		double actualTemperature = temperatureMonitor.readTemperature();
		Assert.assertEquals(tempeValue, actualTemperature, 0.1);
	}

	@Test
	public void testReadTemperatureNormal() {
		// dummy temperature reading
		double temperatureValue = 37.0;
		Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(temperatureValue);

		BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
		double actualTemperature = temperatureMonitor.readTemperature();
		Assert.assertEquals(temperatureValue, actualTemperature, 0.01);
	}

	@Test
	public void testReadTemperatureAbnormallyHigh() {
		// dummy temperature reading
		double temperatureValue = 40.0;
		Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(temperatureValue);

		BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
		double actualTemperature = temperatureMonitor.readTemperature();
		Assert.assertEquals(temperatureValue, actualTemperature, 0.01);

	}

	@Test
	public void testReportTemperatureReadingToCloud() {
		// Mock reportTemperatureReadingToCloud() calls
		// cloudService.sendTemperatureToCloud()

		TemperatureReading temperatureReading = new TemperatureReading();
		BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(null, cloudService, null);
		bodyTemperatureMonitor.reportTemperatureReadingToCloud(temperatureReading);

		// Mockito.times(1) -> the function is called once
		Mockito.verify(cloudService, Mockito.times(1)).sendTemperatureToCloud(temperatureReading);
	}

	@Test
	public void testInquireBodyStatusNormalNotification() {
		Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any())).thenReturn("NORMAL");
		BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(null, cloudService,
				notificationSender);

		bodyTemperatureMonitor.inquireBodyStatus();
		Mockito.verify(notificationSender, Mockito.times(1))
				.sendEmailNotification(bodyTemperatureMonitor.getFixedCustomer(), "Thumbs Up!");

	}

	@Test
	public void testInquireBodyStatusAbnormalNotification() {
		Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any())).thenReturn("ABNORMAL");
		BodyTemperatureMonitor bodyTemperatureMonitor = new BodyTemperatureMonitor(null, cloudService,
				notificationSender);

		bodyTemperatureMonitor.inquireBodyStatus();
		Mockito.verify(notificationSender, Mockito.times(1))
				.sendEmailNotification(bodyTemperatureMonitor.getFamilyDoctor(), "Emergency!");
	}
}
