using Microsoft.EntityFrameworkCore.Design;
using Retrospector.Setup;

namespace Retrospector.DataStorage
{
    public class DesignTimeContextFactory : IDesignTimeDbContextFactory<DatabaseContext>
    {
        public DatabaseContext CreateDbContext(string[] args)
        {
            var config = new ConfigurationLoader().LoadConfig<Configuration>();
            return new DatabaseContext(config);
        }
    }
}