package ru.oxbao.sensorscollect;


public class Solutions
{
    public enum ResultTestEnum
    {
        Good,
        Passable,
        Acceptable,
        Bad
    }

    static public String ToString(ResultTestEnum resultTestEnum)
    {
        if (resultTestEnum == ResultTestEnum.Good) return "Good";
        else if (resultTestEnum == ResultTestEnum.Passable) return "Passable";
        else if (resultTestEnum == ResultTestEnum.Acceptable) return "Acceptable";
        else if (resultTestEnum == ResultTestEnum.Bad) return "Bad";

        return "Unknown";
    }


}
