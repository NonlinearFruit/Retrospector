using System;
using System.Linq;
using FontAwesome.Sharp;
using Retrospector.AchievementTab;
using Retrospector.AchievementTab.Interfaces;
using Retrospector.DataStorage.Models;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.AchievementTab
{
    public class SocialAchievementsTests : DatabaseContextTests
    {
        private readonly IAchievementGenerator _generator;

        protected SocialAchievementsTests()
        {
            _generator = new SocialAchievements(_actContext);
        }

        public class GetAchievements : SocialAchievementsTests
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
                Assert.Equal(IconChar.Users, icon);
            }

            [Fact]
            public void share_the_same_hint()
            {
                var achievements = _generator.GetAchievements();

                var hint = Assert.Single(achievements.Select(a => a.Hint).Distinct());
                Assert.Equal("Retrospecting is a great way to make friends*", hint);
            }

            [Theory]
            [InlineData("Social")]
            [InlineData("Amicable")]
            [InlineData("Gregarious")]
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
            [InlineData("Have 3 users")]
            [InlineData("Have 6 users")]
            [InlineData("Have 10 users")]
            public void one_has_the_description(string description)
            {
                var achievements = _generator.GetAchievements();

                Assert.Contains(description, achievements.Select(a => a.Description));
            }

            [Theory]
            [InlineData(0, 0, 0, 0)]
            [InlineData(1, 33, 16, 10)]
            [InlineData(100, 100, 100, 100)]
            public void progress_is_calculated_correctly(int countOfUsers, params int[] expectedProgresses)
            {
                _assertContext.Reviews.AddRange(countOfUsers, i => new Review{ User = $"User {i}"});
                _assertContext.SaveChanges();

                var achievements = _generator.GetAchievements();

                Assert.Empty(achievements.Select(a => a.Progress).Except(expectedProgresses));
            }
        }
    }
}