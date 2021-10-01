using System.Diagnostics.CodeAnalysis;
using Retrospector.AchievementTab.Interfaces;

namespace Retrospector.Tests.TestDoubles.AchievementTab
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class AchievementTab_TestDouble : IAchievementTab
    {
        public string Header { get; }
    }
}