using System.Collections.Generic;
using Retrospector.Search.Models;
using Retrospector.DataStorage.Models;
using Retrospector.Search.Interfaces;

namespace Retrospector.Search
{
    public class MediaReducer : IMediaReducer
    {
        public Dictionary<RetrospectorAttribute, string> Reduce(Media item)
        {
            return new Dictionary<RetrospectorAttribute, string>
            {
                {RetrospectorAttribute.MediaTitle, item?.Title ?? ""},
                {RetrospectorAttribute.MediaCreator, item?.Creator ?? ""},
                {RetrospectorAttribute.MediaSeason, item?.SeasonId ?? ""},
                {RetrospectorAttribute.MediaEpisode, item?.EpisodeId ?? ""},
                {RetrospectorAttribute.MediaCategory, item?.Category ?? ""},
                {RetrospectorAttribute.MediaDescription, item?.Description ?? ""}
            };
        }
    }
}