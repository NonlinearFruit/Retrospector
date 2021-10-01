using System.Collections.Generic;
using System.Linq;
using Retrospector.AchievementTab;
using Retrospector.AchievementTab.Interfaces;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.AchievementTab
{
    public class AchievementTabViewModelTests
    {
        private readonly AchievementTabViewModel _viewModel;

        protected AchievementTabViewModelTests()
        {
            var generators = new List<IAchievementGenerator>();
            _viewModel = new AchievementTabViewModel(generators);
        }

        public class Constructor : AchievementTabViewModelTests
        {
            [Fact]
            public void sets_header()
            {
                Assert.Equal("Achievements", _viewModel.Header);
            }

            [Theory]
            [InlineDatas(0, 1, 5)]
            public void gets_achievements(int countOfGenerators)
            {
                var generator = new AchievementGenerator_TestDouble
                {
                    ReturnFor_GetAchievements = new List<Achievement>()
                };
                var generators = Enumerable.Repeat(generator, countOfGenerators);

                new AchievementTabViewModel(generators);

                Assert.Equal(countOfGenerators, generator.CountOfCallsTo_GetAchievements);
            }

            [Fact]
            public void stores_achievements()
            {
                var achievement = new Achievement();
                var generator = new AchievementGenerator_TestDouble
                {
                    ReturnFor_GetAchievements = new [] {achievement}
                };

                var viewModel = new AchievementTabViewModel(new [] {generator});

                Assert.Contains(achievement, viewModel.Achievements);
            }
        }
    }
}