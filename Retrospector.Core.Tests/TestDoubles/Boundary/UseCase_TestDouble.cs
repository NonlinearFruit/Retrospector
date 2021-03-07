using System.Diagnostics.CodeAnalysis;
using Retrospector.Core.Boundary;

namespace Retrospector.Core.Tests.TestDoubles.Boundary
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class UseCase_TestDouble<T> : IUseCase<T> where T : IRequest
    {
        public T LastRequestPassedTo_Execute { get; set; }
        public int CountOfCallsTo_Execute { get; set; }
        public void Execute(T request)
        {
            CountOfCallsTo_Execute++;
            LastRequestPassedTo_Execute = request;
        }
    }
}