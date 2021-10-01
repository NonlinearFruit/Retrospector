using System.Collections.Generic;
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
    }
}