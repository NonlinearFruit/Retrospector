using System;
using System.Diagnostics;
using System.IO;
using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Retrospector.Setup;
using Retrospector.Utilities.Interfaces;
using Retrospector.Utilities.Models;

namespace Retrospector.Utilities
{
    public class Logger : ILogger
    {
        private readonly Configuration _config;

        public Logger(Configuration config)
        {
            _config = config;
        }

        public void Log(Severity severity, object message)
        {
            using var stream = BuildStreamWriter(_config.LogFile);
            var log = BuildLogMessage(severity, message);
            Debug.WriteLine(log);
            stream.WriteLine(log);
        }

        private static StreamWriter BuildStreamWriter(string file)
        {
            return File.Exists(file)
                ? File.AppendText(file)
                : File.CreateText(file);
        }

        private static string BuildLogMessage(Severity severity, object message)
        {
            try
            {
                return JsonConvert.SerializeObject(new Message
                {
                    Severity = severity,
                    TimeStamp = DateTime.Now,
                    Body = message
                }, Formatting.Indented, new StringEnumConverter());
            }
            catch (Exception exception)
            {
                return $"[{Severity.Fatal}] Exception throw while trying to log a [{severity}] message of type [{message.GetType()}]: {exception.Message}\n{exception.StackTrace}";
            }
        }

        private class Message
        {
            public Severity Severity { get; init; }
            public DateTime TimeStamp { get; init; }
            public object Body { get; init; }
        }
    }
}