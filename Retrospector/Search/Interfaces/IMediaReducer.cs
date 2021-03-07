using System.Collections.Generic;
using Retrospector.Search.Models;
using Retrospector.DataStorage;

namespace Retrospector.Search.Interfaces
{
    public interface IMediaReducer
    {
        Dictionary<RetrospectorAttribute, string> Reduce(MediaEntity item);
    }
}