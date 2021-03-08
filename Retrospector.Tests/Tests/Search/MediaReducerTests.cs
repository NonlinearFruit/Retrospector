using Retrospector.DataStorage.Models;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.Tests.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.Search
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
        [InlineData(nameof(Media.Title), RetrospectorAttribute.MediaTitle, "Sherlock Holmes")]
        [InlineData(nameof(Media.Creator), RetrospectorAttribute.MediaCreator, "AC Doyle")]
        [InlineData(nameof(Media.SeasonId), RetrospectorAttribute.MediaSeason, "S1")]
        [InlineData(nameof(Media.EpisodeId), RetrospectorAttribute.MediaEpisode, "B1 A Study in Scarlet")]
        [InlineData(nameof(Media.Category), RetrospectorAttribute.MediaCategory, "Book")]
        [InlineData(nameof(Media.Description), RetrospectorAttribute.MediaDescription, "A great book")]
        public void populates_the_attributes(string property, RetrospectorAttribute attribute, object value)
        {
            var media = new Media();
            Reflection.SetProperty(media, property, value);

            var result = _reducer.Reduce(media);

            Assert.Equal($"{value}", result[attribute]);
        }
    }
}