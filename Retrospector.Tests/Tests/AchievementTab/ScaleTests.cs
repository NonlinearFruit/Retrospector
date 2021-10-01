using Retrospector.AchievementTab;
using Xunit;

namespace Retrospector.Tests.Tests.AchievementTab
{
    public class ScaleTests
    {
        [Theory]
        [InlineData(100, 1, 1)]
        [InlineData(50, 1, 2)]
        [InlineData(33, 1, 3)]
        [InlineData(66, 2, 3)]
        [InlineData(100, 3, 3)]
        [InlineData(100, 4, 3)]
        public void works_as_expected(int percent, int value, int max)
        {
            Assert.Equal(percent, Scale.ToPercent(value, max));
        }
    }
}