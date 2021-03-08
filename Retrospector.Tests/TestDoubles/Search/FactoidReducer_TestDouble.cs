using System.Diagnostics.CodeAnalysis;
using Retrospector.DataStorage.Models;
using Retrospector.Search.Interfaces;
using Retrospector.Tests.Utilities;

namespace Retrospector.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class FactoidReducer_TestDouble : Reducer_TestDouble<Factoid>, IFactoidReducer
    { }
}