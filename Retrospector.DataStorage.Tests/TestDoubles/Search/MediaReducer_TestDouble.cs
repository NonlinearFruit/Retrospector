using System.Diagnostics.CodeAnalysis;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Search.Interfaces;
using Retrospector.DataStorage.Tests.Utilities;

namespace Retrospector.DataStorage.Tests.TestDoubles.Search
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class MediaReducer_TestDouble : Reducer_TestDouble<MediaEntity>, IMediaReducer
    { }
}