using Microsoft.EntityFrameworkCore;
using Retrospector.DataStorage.Interfaces;
using Retrospector.DataStorage.Models;
using Retrospector.Setup;

namespace Retrospector.DataStorage
{
    public class DatabaseContext : DbContext, IDatabaseContext
    {
        private readonly Configuration _config;

        public DbSet<Factoid> Factoids { get; set; }
        public DbSet<Review> Reviews { get; set; }
        public DbSet<Media> Media { get; set; }

        public DatabaseContext(Configuration config)
        {
            _config = config;
        }

        protected override void OnConfiguring(DbContextOptionsBuilder options)
        {
            options.UseSqlServer(_config.ConnectionString);
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
        }

        public void RunMigrations()
        {
            base.Database.Migrate();
        }
    }
}