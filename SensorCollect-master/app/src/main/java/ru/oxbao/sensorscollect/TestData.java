package ru.oxbao.sensorscollect;

public class TestData
{
    public double MaxVX, MaxVY, MaxVZ, MaxVal; // temporary

    public double[] XAxis;
    public double[] YAxis;
    public double[] ZAxis;
    public long[] TimeInNanoSeconds;
    private int ArraySize;

    public TestData(int arraySize)
    {
        ArraySize = arraySize;
        this.XAxis = new double[arraySize];
        this.YAxis = new double[arraySize];
        this.ZAxis = new double[arraySize];
        this.TimeInNanoSeconds = new long[arraySize];
    }

    public int GetSizeOfData()
    {
        return ArraySize;
    }

    public String ToString()
    {
        /*
        return "TestData{" +
                "XAxis=" + Arrays.toString(XAxis) +
                ", YAxis=" + Arrays.toString(YAxis) +
                ", ZAxis=" + Arrays.toString(ZAxis) +
                ", TimeInMilliseconds=" + Arrays.toString(TimeInMilliseconds) +
                '}';
        */
        return "maxVX: " + Double.toString(MaxVX) + "; maxVY: " + Double.toString(MaxVY) +
                "; maxVZ: " + Double.toString(MaxVZ) + " ";
    }
}
