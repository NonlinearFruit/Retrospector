using Microsoft.EntityFrameworkCore;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Reviews.Entities;

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

        public DbSet<FactoidEntity> Factoids { get; set; }
        public DbSet<ReviewEntity> Reviews { get; set; }
        public DbSet<MediaEntity> Media { get; set; }
    }
}