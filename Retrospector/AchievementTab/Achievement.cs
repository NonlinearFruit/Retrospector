using FontAwesome.Sharp;

namespace Retrospector.AchievementTab
{
    public class Achievement
    {
        public const string Gold = "Gold";
        public const string Silver = "Silver";
        public const string Bronze = "SaddleBrown";

        public string Title { get; set; }
        public string Description { get; set; }
        public string Hint { get; set; }
        public IconChar FontAwesomeSymbol { get; set; }
        public string Color { get; set; }
        public int Progress { get; set; }
        public bool IsAchieved => Progress == 100;
        public bool IsInProgress => !IsAchieved;
    }
}