using System.Diagnostics.CodeAnalysis;
using Retrospector.DataStorage;
using Retrospector.Search.Interfaces;
using Retrospector.Tests.Utilities;

namespace Retrospector.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class MediaReducer_TestDouble : Reducer_TestDouble<MediaEntity>, IMediaReducer
    { }
}