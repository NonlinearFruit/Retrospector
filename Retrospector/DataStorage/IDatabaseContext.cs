using Microsoft.EntityFrameworkCore;

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