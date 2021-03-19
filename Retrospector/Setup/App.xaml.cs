using System;
using System.Windows;
using System.Windows.Threading;
using Microsoft.Extensions.DependencyInjection;
using Retrospector.Utilities.Interfaces;
using Retrospector.Utilities.Models;

namespace Retrospector.Setup
{
    public partial class App
    {
        private readonly IServiceProvider _provider;
        private readonly Startup _startup;

        public App()
        {
            var services = new ServiceCollection();
            _startup = new Startup(new ConfigurationLoader().LoadConfig<Configuration>());
            _provider = _startup.ConfigureServices(services).BuildServiceProvider();
        }
        private void OnStartup(object sender, StartupEventArgs e)
        {
            _startup.Configure(_provider);
        }

        private void UnhandledExceptionCatcher(object sender, DispatcherUnhandledExceptionEventArgs args)
        {
            var logger = _provider.GetService<ILogger>();
            logger.Log(Severity.Fatal, args.Exception);
        }
    }
}