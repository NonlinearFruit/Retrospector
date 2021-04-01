using System;
using System.Linq;
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
            modelBuilder
                .Entity<Media>()
                .Property(m => m.CreatedDate)
                .HasDefaultValueSql("sysutcdatetime()");

            modelBuilder
                .Entity<MediaTypeEntity>()
                .Property(t => t.CreatedDate)
                .HasDefaultValueSql("sysutcdatetime()");

            modelBuilder
                .Entity<MediaTypeEntity>()
                .HasData(Enum
                    .GetValues<MediaType>()
                    .Select(e => new MediaTypeEntity
                    {
                        Id = (int) e,
                        Name = e.ToString()
                    }));

            modelBuilder
                .Entity<Review>()
                .Property(r => r.CreatedDate)
                .HasDefaultValueSql("sysutcdatetime()");

            modelBuilder
                .Entity<Factoid>()
                .Property(f => f.CreatedDate)
                .HasDefaultValueSql("sysutcdatetime()");
        }

        public void RunMigrations()
        {
            base.Database.Migrate();
        }
    }
}