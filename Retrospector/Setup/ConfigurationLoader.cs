using System.Diagnostics;
using System.IO;
using Microsoft.Extensions.Configuration;

namespace Retrospector.Setup
{
    public class ConfigurationLoader
    {
        public T LoadConfig<T>() where T : new()
            => new ConfigurationBuilder()
#if DEBUG
                .AddJsonFile(@"retrospector-settings.json", optional: false)
#else
                .SetBasePath(GetBasePath())
                .AddJsonFile("retrospector-settings.json", optional: true)
#endif
                .Build()
                .Get<T>();

        private static string GetBasePath()
        {
            using var processModule = Process.GetCurrentProcess().MainModule;
            var path = Path.GetDirectoryName(processModule?.FileName);
            return path;
        }
    }
}