using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using Retrospector.AchievementTab;
using Retrospector.AchievementTab.Interfaces;

namespace Retrospector.Tests.TestDoubles.AchievementTab
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class AchievementGenerator_TestDouble : IAchievementGenerator
    {
        public int CountOfCallsTo_GetAchievements { get; set; }
        public ICollection<Achievement> ReturnFor_GetAchievements { get; set; }
        public ICollection<Achievement> GetAchievements()
        {
            CountOfCallsTo_GetAchievements++;
            return ReturnFor_GetAchievements;
        }
    }
}