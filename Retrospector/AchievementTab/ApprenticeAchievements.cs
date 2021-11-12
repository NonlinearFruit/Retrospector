using System;
using System.Collections.Generic;
using System.Linq;
using FontAwesome.Sharp;
using Retrospector.AchievementTab.Interfaces;
using Retrospector.DataStorage.Interfaces;
using Retrospector.Setup;

namespace Retrospector.AchievementTab
{
    public class ApprenticeAchievements : IAchievementGenerator
    {
        private readonly IDatabaseContext _context;
        private readonly Configuration _config;

        public ApprenticeAchievements(IDatabaseContext context, Configuration config)
        {
            _context = context;
            _config = config;
        }

        public ICollection<Achievement> GetAchievements()
        {
            var (gold, silver, bronze) = (10, 5, 1);
            var icon = IconChar.FortAwesome;
            var hint = "Give it time";
            var age = CalculateAge();
            return new List<Achievement>
            {
                new()
                {
                    Title = "Master",
                    Color = Achievement.Gold,
                    Description = $"Retrospect for {gold} years",
                    Hint = hint,
                    FontAwesomeSymbol = icon,
                    Progress = Scale.ToPercent(age, gold)
                },
                new()
                {
                    Title = "Journeyman",
                    Color = Achievement.Silver,
                    Description = $"Retrospect for {silver} years",
                    Hint = hint,
                    FontAwesomeSymbol = icon,
                    Progress = Scale.ToPercent(age, silver)
                },
                new()
                {
                    Title = "Apprentice",
                    Color = Achievement.Bronze,
                    Description = $"Retrospect for {bronze} year",
                    Hint = hint,
                    FontAwesomeSymbol = icon,
                    Progress = Scale.ToPercent(age, bronze)
                }
            };
        }

        private int CalculateAge()
        {
            try
            {
                var now = DateTime.Now;
                var oldestReview = _context
                    .Reviews
                    .Where(r => r.User == _config.DefaultUser)
                    .Select(r => r.Date)
                    .Min();
                var age = now.Year - oldestReview.Year;
                if (oldestReview.Date > now.AddYears(-age)) age--;
                return age;
            }
            catch (Exception)
            {
                return 0;
            }
        }
    }
}