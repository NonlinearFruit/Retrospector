using Retrospector.Utilities.Models;

namespace Retrospector.Utilities.Interfaces
{
    public interface ILogger
    {
        void Log(Severity severit, object message);
    }
}