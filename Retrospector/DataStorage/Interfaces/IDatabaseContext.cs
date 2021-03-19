using Microsoft.EntityFrameworkCore;
using Retrospector.DataStorage.Models;

namespace Retrospector.DataStorage.Interfaces
{
    public interface IDatabaseContext
    {
        DbSet<Factoid> Factoids { get; set; }
        DbSet<Review> Reviews { get; set; }
        DbSet<Media> Media { get; set; }
        int SaveChanges();
        void RunMigrations();
    }
}