package scripts;


public class KTimer
{

    public KTimer(long timeToRun)
    {
        startTime = 0L;
        endTime = 0L;
        this.timeToRun = 0L;
        if(timeToRun < 0L)
            timeToRun *= -1L;
        startTime = System.currentTimeMillis();
        endTime = startTime + timeToRun;
        this.timeToRun = timeToRun;
    }

    public KTimer()
    {
        startTime = 0L;
        endTime = 0L;
        timeToRun = 0L;
        startTime = System.currentTimeMillis();
        endTime = startTime;
    }

    public void addTime(long timeToRun)
    {
        endTime += timeToRun;
    }

    public void newEndTime(long timeToRun)
    {
        endTime = System.currentTimeMillis() + timeToRun;
    }

    public void reset()
    {
        startTime = System.currentTimeMillis();
        endTime = startTime + timeToRun;
    }

    public boolean isDone()
    {
        return endTime <= System.currentTimeMillis();
    }

    public long getTimeRemaining()
    {
        return endTime - System.currentTimeMillis();
    }

    public long getTimeElapsed()
    {
        return System.currentTimeMillis() - startTime;
    }

    public String getFormattedTime(long timeMilliseconds)
    {
        StringBuilder b = new StringBuilder();
        long runtime = timeMilliseconds;
        long TotalSecs = runtime / 1000L;
        long TotalMins = TotalSecs / 60L;
        long TotalHours = TotalMins / 60L;
        int seconds = (int)runtime % 60;
        int minutes = (int)TotalMins % 60;
        int hours = (int)TotalHours % 60;
        if(hours < 10)
            b.append("0");
        b.append(hours);
        b.append(" : ");
        if(minutes < 10)
            b.append("0");
        b.append(minutes);
        b.append(" : ");
        if(seconds < 10)
            b.append("0");
        b.append(seconds);
        return b.toString();
    }

    private long startTime;
    long endTime;
    private long timeToRun;
}
