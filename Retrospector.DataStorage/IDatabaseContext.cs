using Microsoft.EntityFrameworkCore;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Reviews.Entities;

namespace Retrospector.DataStorage
{
    public interface IDatabaseContext
    {
        DbSet<FactoidEntity> Factoids { get; set; }
        DbSet<ReviewEntity> Reviews { get; set; }
        DbSet<MediaEntity> Media { get; set; }
        int SaveChanges();
    }
}