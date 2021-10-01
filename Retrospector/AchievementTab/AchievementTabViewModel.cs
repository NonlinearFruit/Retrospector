using System.Collections.Generic;
using System.Linq;
using Retrospector.AchievementTab.Interfaces;

namespace Retrospector.AchievementTab
{
    public class AchievementTabViewModel : IAchievementTab
    {
        public AchievementTabViewModel(IEnumerable<IAchievementGenerator> generators)
        {
            foreach (var generator in generators)
                foreach (var achievement in generator.GetAchievements())
                    Achievements.Add(achievement);
        }

        public string Header { get; } = "Achievements";
        public ICollection<Achievement> Achievements { get; } = new List<Achievement>();
        public int CountOfGoldAchievements => Achievements
            .Where(a => a.IsAchieved)
            .Count(a => a.Color == Achievement.Gold);
        public int CountOfSilverAchievements => Achievements
            .Where(a => a.IsAchieved)
            .Count(a => a.Color == Achievement.Silver);
        public int CountOfBronzeAchievements => Achievements
            .Where(a => a.IsAchieved)
            .Count(a => a.Color == Achievement.Bronze);
        public int CountOfLockAchievements => Achievements.Count(a => a.IsInProgress);
    }
}