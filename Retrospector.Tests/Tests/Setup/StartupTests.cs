using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Extensions.DependencyInjection;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.DataStorage.Interfaces;
using Retrospector.DataStorage.Models;
using Retrospector.Main;
using Retrospector.Main.Interfaces;
using Retrospector.Setup;
using Retrospector.Tests.TestDoubles.DataStorage;
using Retrospector.Tests.TestDoubles.Main;
using Retrospector.Tests.Utilities;
using Retrospector.Utilities.Interfaces;
using Xunit;

namespace Retrospector.Tests.Tests.Setup
{
    public class StartupTests
    {
        private readonly Startup _startup;

        public static IEnumerable<object[]> RegisteredTypes => new Startup(new Configuration())
                .ConfigureServices(new ServiceCollection())
                .Select(s => s.ServiceType)
                .Where(t => new [] {typeof(Startup).Assembly, typeof(Media).Assembly, typeof(BinaryOperator).Assembly}.Contains(t.Assembly))
                .Select(t => new object[] {t});

        protected StartupTests()
        {
            _startup = new Startup(new Configuration());
        }

        public class Constructor : StartupTests
        {
            [Fact]
            public void stores_database_config_on_property()
            {
                var config = new Configuration();

                var startup = new Startup(config);

                Assert.Equal(config, startup.Configuration);
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

                try
                {
                    provider.GetService(service);
                }
                catch (InvalidOperationException exception)
                {
                    Assert.Equal("The calling thread must be STA, because many UI components require this.", exception.Message);
                }
            }

            [Theory]
            [InlineData(typeof(Configuration), ServiceLifetime.Singleton)]
            [InlineData(typeof(IDatabaseContext), ServiceLifetime.Transient)]
            [InlineData(typeof(IFactoidReducer), ServiceLifetime.Transient)]
            [InlineData(typeof(ILeafExpressionBuilder), ServiceLifetime.Transient)]
            [InlineData(typeof(ILeafOperator), ServiceLifetime.Transient)]
            [InlineData(typeof(ILogger), ServiceLifetime.Transient)]
            [InlineData(typeof(IMediaReducer), ServiceLifetime.Transient)]
            [InlineData(typeof(IQueryBuilder), ServiceLifetime.Transient)]
            [InlineData(typeof(IReviewReducer), ServiceLifetime.Transient)]
            [InlineData(typeof(ISearchDataGateway), ServiceLifetime.Transient)]
            [InlineData(typeof(ISearchFilterBuilder), ServiceLifetime.Transient)]
            [InlineData(typeof(IMainWindow), ServiceLifetime.Transient)]
            [InlineData(typeof(MainWindowViewModel), ServiceLifetime.Transient)]
            public void has_service(Type service, ServiceLifetime lifetime)

            {
                var services = _startup.ConfigureServices(new ServiceCollection());

                var registration = Assert.Single(services.Where(s => s.ServiceType == service));
                Assert.Equal(lifetime, registration.Lifetime);
            }

            [Fact]
            public void uses_stored_database_config()
            {
                var config = new Configuration();
                _startup.Configuration = config;

                var services = _startup.ConfigureServices(new ServiceCollection());
                var provider = services.BuildServiceProvider();

                Assert.Equal(config, provider.GetService<Configuration>());
            }
        }

        public class Configure : StartupTests
        {
            [Fact]
            public void shows_main_window()
            {
                var window = new MainWindow_TestDouble();
                var provider = ArrangeProvider(window, new DatabaseContext_TestDouble());

                _startup.Configure(provider);

                Assert.Equal(Verify.Once, window.CountOfCallsTo_Show);
            }

            [Fact]
            public void runs_migrations_on_database()
            {
                var context = new DatabaseContext_TestDouble();
                var provider = ArrangeProvider(new MainWindow_TestDouble(), context);

                _startup.Configure(provider);

                Assert.Equal(Verify.Once, context.CountOfCallsTo_RunMigrations);
            }

            [Fact]
            public void runs_migrations_before_showing_window()
            {
                var context = new DatabaseContext_TestDouble();
                var window = new MainWindow_TestDouble
                {
                    ExceptionToThrowFor_Show = new Exception()
                };
                var provider = ArrangeProvider(window, context);

                try
                {
                    _startup.Configure(provider);
                }
                catch (Exception)
                {
                    // ignored
                }

                Assert.Equal(Verify.Once, context.CountOfCallsTo_RunMigrations);
            }

            private IServiceProvider ArrangeProvider(IMainWindow window = null, IDatabaseContext context = null)
            {
                return new ServiceCollection()
                    .AddSingleton(window)
                    .AddSingleton(context)
                    .BuildServiceProvider();
            }
        }
    }
}