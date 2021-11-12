using System.Collections.Generic;
using System.Linq;
using Retrospector.AchievementTab;
using Retrospector.AchievementTab.Interfaces;
using Retrospector.Tests.TestDoubles.AchievementTab;
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

        public class AchievementsCounts : AchievementTabViewModelTests
        {
            [Theory]
            [InlineData(0, Achievement.Gold, 0)]
            [InlineData(1, Achievement.Gold, 1)]
            [InlineData(5, Achievement.Gold, 5)]
            [InlineData(1, Achievement.Silver, 0)]
            [InlineData(1, Achievement.Bronze, 0)]
            public void gold_count_is_correct(int countOfAchievements, string colorOfAchievements, int expectedCount)
            {
                for (var i = 0; i < countOfAchievements; i++)
                    _viewModel.Achievements.Add(new Achievement
                    {
                        Color = colorOfAchievements,
                        Progress = 100
                    });

                Assert.Equal(expectedCount, _viewModel.CountOfGoldAchievements);
            }

            [Fact]
            public void gold_count_ignores_in_progress()
            {
                _viewModel.Achievements.Add(new Achievement
                {
                    Color = Achievement.Gold,
                    Progress = 99
                });

                Assert.Equal(0, _viewModel.CountOfGoldAchievements);
            }

            [Theory]
            [InlineData(0, Achievement.Silver, 0)]
            [InlineData(1, Achievement.Silver, 1)]
            [InlineData(5, Achievement.Silver, 5)]
            [InlineData(1, Achievement.Gold, 0)]
            [InlineData(1, Achievement.Bronze, 0)]
            public void silver_count_is_correct(int countOfAchievements, string colorOfAchievements, int expectedCount)
            {
                for (var i = 0; i < countOfAchievements; i++)
                    _viewModel.Achievements.Add(new Achievement
                    {
                        Color = colorOfAchievements,
                        Progress = 100
                    });

                Assert.Equal(expectedCount, _viewModel.CountOfSilverAchievements);
            }

            [Fact]
            public void silver_count_ignores_in_progress()
            {
                _viewModel.Achievements.Add(new Achievement
                {
                    Color = Achievement.Silver,
                    Progress = 99
                });

                Assert.Equal(0, _viewModel.CountOfSilverAchievements);
            }

            [Theory]
            [InlineData(0, Achievement.Bronze, 0)]
            [InlineData(1, Achievement.Bronze, 1)]
            [InlineData(5, Achievement.Bronze, 5)]
            [InlineData(1, Achievement.Silver, 0)]
            [InlineData(1, Achievement.Gold, 0)]
            public void bronze_count_is_correct(int countOfAchievements, string colorOfAchievements, int expectedCount)
            {
                for (var i = 0; i < countOfAchievements; i++)
                    _viewModel.Achievements.Add(new Achievement
                    {
                        Color = colorOfAchievements,
                        Progress = 100
                    });

                Assert.Equal(expectedCount, _viewModel.CountOfBronzeAchievements);
            }

            [Fact]
            public void bronze_count_ignores_in_progress()
            {
                _viewModel.Achievements.Add(new Achievement
                {
                    Color = Achievement.Bronze,
                    Progress = 99
                });

                Assert.Equal(0, _viewModel.CountOfBronzeAchievements);
            }

            [Theory]
            [InlineData(0, 0, 0)]
            [InlineData(1, 50, 1)]
            [InlineData(5, 75, 5)]
            [InlineData(1, 100, 0)]
            public void lock_count_is_correct(int countOfAchievements, int progress, int expectedCount)
            {
                for (var i = 0; i < countOfAchievements; i++)
                    _viewModel.Achievements.Add(new Achievement{Progress = progress});

                Assert.Equal(expectedCount, _viewModel.CountOfLockAchievements);
            }
        }
    }
}