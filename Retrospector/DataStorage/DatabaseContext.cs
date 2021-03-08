using Microsoft.EntityFrameworkCore;
using Retrospector.DataStorage.Interfaces;
using Retrospector.DataStorage.Models;

namespace Retrospector.DataStorage
{
    public class DatabaseContext : DbContext, IDatabaseContext
    {
        private readonly DatabaseConfiguration _config;

        public DatabaseContext(DatabaseConfiguration config)
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

        public DbSet<Factoid> Factoids { get; set; }
        public DbSet<Review> Reviews { get; set; }
        public DbSet<Media> Media { get; set; }
    }
}