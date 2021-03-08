using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Extensions.DependencyInjection;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.DataStorage.Interfaces;
using Retrospector.DataStorage.Models;
using Retrospector.Setup;
using Xunit;

namespace Retrospector.Tests.Tests.Setup
{
    public class StartupTests
    {
        private readonly Startup _startup;

        public static IEnumerable<object[]> RegisteredTypes => new Startup(new DatabaseConfiguration())
                .ConfigureServices(new ServiceCollection())
                .Select(s => s.ServiceType)
                .Where(t => new [] {typeof(Startup).Assembly, typeof(Media).Assembly, typeof(BinaryOperator).Assembly}.Contains(t.Assembly))
                .Select(t => new object[] {t});

        public StartupTests()
        {
            _startup = new Startup(new DatabaseConfiguration());
        }

        public class Constructor : StartupTests
        {
            [Fact]
            public void stores_database_config_on_property()
            {
                var config = new DatabaseConfiguration();

                var startup = new Startup(config);

                Assert.Equal(config, startup.DatabaseConfig);
            }
        }

        public class ConfigureServices : StartupTests
        {
            [Theory]
            [MemberData(nameof(RegisteredTypes))]
            public void services_are_resolvable(Type service)
            {
                var services = _startup.ConfigureServices(new ServiceCollection());
                var provider = services.BuildServiceProvider();

                provider.GetService(service);
            }

            [Theory]
            [InlineData(typeof(DatabaseConfiguration), ServiceLifetime.Singleton)]
            [InlineData(typeof(IDatabaseContext), ServiceLifetime.Transient)]
            [InlineData(typeof(IFactoidReducer), ServiceLifetime.Transient)]
            [InlineData(typeof(ILeafExpressionBuilder), ServiceLifetime.Transient)]
            [InlineData(typeof(ILeafOperator), ServiceLifetime.Transient)]
            [InlineData(typeof(IMediaReducer), ServiceLifetime.Transient)]
            [InlineData(typeof(ISearchDataGateway), ServiceLifetime.Transient)]
            [InlineData(typeof(IQueryBuilder), ServiceLifetime.Transient)]
            [InlineData(typeof(IReviewReducer), ServiceLifetime.Transient)]
            [InlineData(typeof(ISearchFilterBuilder), ServiceLifetime.Transient)]
            public void has_service(Type service, ServiceLifetime lifetime)

            {
                var services = _startup.ConfigureServices(new ServiceCollection());

                var registration = Assert.Single(services.Where(s => s.ServiceType == service));
                Assert.Equal(lifetime, registration.Lifetime);
            }

            [Fact]
            public void uses_stored_database_config()
            {
                var config = new DatabaseConfiguration();
                _startup.DatabaseConfig = config;

                var services = _startup.ConfigureServices(new ServiceCollection());
                var provider = services.BuildServiceProvider();

                Assert.Equal(config, provider.GetService<DatabaseConfiguration>());
            }
        }
    }
}