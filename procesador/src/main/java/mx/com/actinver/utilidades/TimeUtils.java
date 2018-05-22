package mx.com.actinver.utilidades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.S");
	private LocalDateTime todayMidnight;
	private Long lastTimeStamp;
	
	public void inicializar() {
		LocalTime midnight = LocalTime.MIDNIGHT;
		LocalDate today = LocalDate.now(ZoneId.of("UTC-6"));
		this.todayMidnight = LocalDateTime.of(today, midnight);
	}
	
	public String getTimeFomMidnightPlusSeconds(Long seconds) {
		LocalDateTime newTime = this.todayMidnight.plusSeconds(seconds);
		return newTime.format(formatter);
	}
	
	public String getTimeFromMidnightPlusSecondsAndNanoSeconds(Long seconds, Long nanoSeconds) {
		seconds = seconds != null ? seconds : 0;
		nanoSeconds = nanoSeconds != null ? nanoSeconds : 0;
		LocalDateTime newTime = this.todayMidnight.plusSeconds(seconds).plusNanos(nanoSeconds);
		return newTime.format(formatter);
	}

	public Long getLastTimeStamp() {
		return lastTimeStamp;
	}

	public void setLastTimeStamp(Long lastTimeStamp) {
		this.lastTimeStamp = lastTimeStamp;
	}

}