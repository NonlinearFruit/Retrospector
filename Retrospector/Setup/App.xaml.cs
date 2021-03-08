using System;
using System.Windows;
using System.Windows.Threading;
using Microsoft.Extensions.DependencyInjection;
using Retrospector.DataStorage.Models;
using Retrospector.Main;

namespace Retrospector.Setup
{
    public partial class App
    {
        private IServiceProvider _provider;

        public App()
        {
            var services = new ServiceCollection();
            var startup = new Startup(new DatabaseConfiguration());
            _provider = startup.ConfigureServices(services).BuildServiceProvider();
        }
        private void OnStartup(object sender, StartupEventArgs e)
        {
            var window = _provider.GetRequiredService<MainWindow>();
            window.Show();
        }

        private void UnhandledExceptionCatcher(object sender, DispatcherUnhandledExceptionEventArgs e)
        {
            throw new System.NotImplementedException();
        }
    }
}