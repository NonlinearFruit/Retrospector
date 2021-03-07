using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Search;
using Retrospector.DataStorage.Search.Interfaces;
using Retrospector.DataStorage.Tests.Utilities;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Search
{
    public class MediaReducerTests
    {
        private IMediaReducer _reducer;

        public MediaReducerTests()
        {
            _reducer = new MediaReducer();
        }

        [Theory]
        [InlineData(RetrospectorAttribute.MediaTitle)]
        [InlineData(RetrospectorAttribute.MediaCreator)]
        [InlineData(RetrospectorAttribute.MediaSeason)]
        [InlineData(RetrospectorAttribute.MediaEpisode)]
        [InlineData(RetrospectorAttribute.MediaCategory)]
        [InlineData(RetrospectorAttribute.MediaDescription)]
        public void returns_the_proper_attributes(RetrospectorAttribute attribute)
        {
            var result = _reducer.Reduce(null);

            Assert.Contains(attribute, result.Keys);
        }

        [Theory]
        [InlineData(RetrospectorAttribute.MediaTitle)]
        [InlineData(RetrospectorAttribute.MediaCreator)]
        [InlineData(RetrospectorAttribute.MediaSeason)]
        [InlineData(RetrospectorAttribute.MediaEpisode)]
        [InlineData(RetrospectorAttribute.MediaCategory)]
        [InlineData(RetrospectorAttribute.MediaDescription)]
        public void defaults_to_empty_string(RetrospectorAttribute attribute)
        {
            var result = _reducer.Reduce(null);

            Assert.Equal("", result[attribute]);
        }

        [Theory]
        [InlineData(nameof(MediaEntity.Title), RetrospectorAttribute.MediaTitle, "Sherlock Holmes")]
        [InlineData(nameof(MediaEntity.Creator), RetrospectorAttribute.MediaCreator, "AC Doyle")]
        [InlineData(nameof(MediaEntity.SeasonId), RetrospectorAttribute.MediaSeason, "S1")]
        [InlineData(nameof(MediaEntity.EpisodeId), RetrospectorAttribute.MediaEpisode, "B1 A Study in Scarlet")]
        [InlineData(nameof(MediaEntity.Category), RetrospectorAttribute.MediaCategory, "Book")]
        [InlineData(nameof(MediaEntity.Description), RetrospectorAttribute.MediaDescription, "A great book")]
        public void populates_the_attributes(string property, RetrospectorAttribute attribute, object value)
        {
            var media = new MediaEntity();
            Reflection.SetProperty(media, property, value);

            var result = _reducer.Reduce(media);

            Assert.Equal($"{value}", result[attribute]);
        }
    }
}