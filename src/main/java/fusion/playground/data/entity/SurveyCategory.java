package fusion.playground.data.entity;

public enum SurveyCategory
{
    example,
    daily,
    weekly,
    monthly,
    lucky;

//    public static String[] names()
//    {
//        return Stream.of(QuestionCategory.values()).map(QuestionCategory::name).toArray(String[]::new);
//    }

    public static SurveyCategory createFrom(String string)
    {
        SurveyCategory[] values = SurveyCategory.values();
        for (SurveyCategory value : values)
        {
            if (string.equals(value.toString()))
            {
                return value;
            }
        }

        return null;
    }

}
