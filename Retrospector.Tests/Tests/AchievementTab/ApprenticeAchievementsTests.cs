using System;
using System.Linq;
using FontAwesome.Sharp;
using Retrospector.AchievementTab;
using Retrospector.AchievementTab.Interfaces;
using Retrospector.DataStorage.Models;
using Retrospector.Setup;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.AchievementTab
{
    public class ApprenticeAchievementsTests : DatabaseContextTests
    {
        private readonly IAchievementGenerator _generator;
        private Configuration _config;

        protected ApprenticeAchievementsTests()
        {
            _config = new Configuration{DefaultUser = "Neb"};
            _generator = new ApprenticeAchievements(_actContext, _config);
        }

        public class GetAchievements : ApprenticeAchievementsTests
        {
            [Fact]
            public void has_proper_number_of_achievements()
            {
                var achievements = _generator.GetAchievements();

                Assert.Equal(3, achievements.Count);
            }

            [Fact]
            public void share_the_same_symbol()
            {
                var achievements = _generator.GetAchievements();

                var icon = Assert.Single(achievements.Select(a => a.FontAwesomeSymbol).Distinct());
                Assert.Equal(IconChar.FortAwesome, icon);
            }

            [Fact]
            public void share_the_same_hint()
            {
                var achievements = _generator.GetAchievements();

                var hint = Assert.Single(achievements.Select(a => a.Hint).Distinct());
                Assert.Equal("Give it time", hint);
            }

            [Theory]
            [InlineData("Apprentice")]
            [InlineData("Journeyman")]
            [InlineData("Master")]
            public void one_has_the_title(string title)
            {
                var achievements = _generator.GetAchievements();

                Assert.Contains(title, achievements.Select(a => a.Title));
            }

            [Theory]
            [InlineData(Achievement.Gold)]
            [InlineData(Achievement.Silver)]
            [InlineData(Achievement.Bronze)]
            public void one_has_the_color(string color)
            {
                var achievements = _generator.GetAchievements();

                Assert.Contains(color, achievements.Select(a => a.Color));
            }

            [Fact]
            public void colors_are_ordered()
            {
                var achievements = _generator.GetAchievements();

                var colors = achievements.Select(a => a.Color).ToList();
                var goldIndex = colors.IndexOf(Achievement.Gold);
                var silverIndex = colors.IndexOf(Achievement.Silver);
                var bronzeIndex = colors.IndexOf(Achievement.Bronze);
                Assert.True(goldIndex < silverIndex);
                Assert.True(silverIndex < bronzeIndex);
            }

            [Theory]
            [InlineData("Retrospect for 1 year")]
            [InlineData("Retrospect for 5 years")]
            [InlineData("Retrospect for 10 years")]
            public void one_has_the_description(string description)
            {
                var achievements = _generator.GetAchievements();

                Assert.Contains(description, achievements.Select(a => a.Description));
            }

            [Theory]
            [InlineData(0, 0, 0, 0)]
            [InlineData(1, 100, 20, 10)]
            [InlineData(5, 100, 100, 50)]
            [InlineData(10, 100, 100, 100)]
            [InlineData(11, 100, 100, 100)]
            public void progress_is_calculated_correctly(int age, params int[] expectedProgresses)
            {
                _assertContext.Reviews.Add(new Review
                {
                    User = _config.DefaultUser,
                    Date = DateTime.Now.AddYears(-age)
                });
                _assertContext.SaveChanges();

                var achievements = _generator.GetAchievements();

                Assert.Empty(achievements.Select(a => a.Progress).Except(expectedProgresses));
            }

            [Fact]
            public void ignores_reviews_from_wrong_user()
            {
                _assertContext.Reviews.Add(new Review
                {
                    User = $"Not {_config.DefaultUser}",
                    Date = DateTime.Now.AddYears(-100)
                });
                _assertContext.SaveChanges();

                var achievements = _generator.GetAchievements();

                Assert.All(achievements, a => Assert.Equal(0, a.Progress));
            }
        }
    }
}