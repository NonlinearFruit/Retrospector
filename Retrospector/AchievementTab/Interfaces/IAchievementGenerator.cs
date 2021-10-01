using System.Collections.Generic;

namespace Retrospector.AchievementTab.Interfaces
{
    public interface IAchievementGenerator
    {
        ICollection<Achievement> GetAchievements();
    }
}