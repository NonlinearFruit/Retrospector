using System.Collections.Generic;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Medias.Entities;

namespace Retrospector.DataStorage.Search.Interfaces
{
    public interface IMediaReducer
    {
        Dictionary<RetrospectorAttribute, string> Reduce(MediaEntity item);
    }
}