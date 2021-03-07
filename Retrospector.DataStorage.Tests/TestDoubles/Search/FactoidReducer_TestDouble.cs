using System.Diagnostics.CodeAnalysis;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Search.Interfaces;
using Retrospector.DataStorage.Tests.Utilities;

namespace Retrospector.DataStorage.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class FactoidReducer_TestDouble : Reducer_TestDouble<FactoidEntity>, IFactoidReducer
    { }
}