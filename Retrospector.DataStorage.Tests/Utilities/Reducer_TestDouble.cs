using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using Retrospector.Core.Search.Models;

namespace Retrospector.DataStorage.Tests.Utilities
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class Reducer_TestDouble<T>
    {
        public Dictionary<RetrospectorAttribute, string> ReturnFor_Reduce { get; set; }
        public T LastItemPassedTo_Reduce { get; set; }
        public int CountOf_Reduce_Calls { get; set; }
        public Dictionary<RetrospectorAttribute, string> Reduce(T item)
        {
            CountOf_Reduce_Calls++;
            LastItemPassedTo_Reduce = item;
            return ReturnFor_Reduce;
        }
    }
}