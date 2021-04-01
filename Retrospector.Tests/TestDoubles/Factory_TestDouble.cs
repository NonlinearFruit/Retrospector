using System;
using System.Diagnostics.CodeAnalysis;

namespace Retrospector.Tests.TestDoubles
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class Factory_TestDouble<T>
    {
        public Func<T> ReturnFor_Factory { get; set; }
        public int CountOfCallsTo_Factory { get; set; }
        public Func<T> Factory()
        {
            CountOfCallsTo_Factory++;
            return ReturnFor_Factory;
        }
    }
}