using System.Collections.Generic;
using System.Linq;
using Retrospector.Core.Boundary;
using Retrospector.Core.Search;
using Retrospector.Core.Search.Models;
using Retrospector.DataStorage.Factoids.Entities;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Reviews.Entities;
using Retrospector.DataStorage.Search;
using Retrospector.DataStorage.Tests.TestDoubles.External;
using Retrospector.DataStorage.Tests.Utilities;
using Xunit;

namespace Retrospector.DataStorage.Tests.Tests.Search
{
    public class SearchIntegrationTests : DatabaseContextTests
    {
        private const string MediaTitle = "T";
        private const string MediaCreator = "C";
        private const string MediaSeason = "S";
        private const string MediaEpisode = "E";
        private const string MediaCategory = "A";
        private const string MediaDescription = "P";
        private const string ReviewRating = "#";
        private const string ReviewUser = "U";
        private const string ReviewDate = "D";
        private const string ReviewContent = "R";
        private const string FactoidTitle = "I";
        private const string FactoidContent = "O";
        private const string Equal = "=";
        private const string GreaterThan = ">";
        private const string LessThan = "<";
        private const string Contains = "~";
        private const string Leaf = "`";
        private const string And = ":";
        private const string Or = "|";
        private const string Not = "!";

        private readonly IUseCase<SearchRequest> _useCase;
        private readonly SearchPresenter_TestDouble _presenter;

        public SearchIntegrationTests()
        {
            _presenter = new SearchPresenter_TestDouble();
            var dataGateway = new SearchDataGateway(
                new MediaReducer(),
                new ReviewReducer(),
                new FactoidReducer(),
                _actContext,
                new SearchFilterBuilder(new LeafExpressionBuilder()));
            var commandDecoder = new Dictionary<string, RetrospectorAttribute>
            {
                {MediaTitle, RetrospectorAttribute.MediaTitle},
                {MediaCreator, RetrospectorAttribute.MediaCreator},
                {MediaSeason, RetrospectorAttribute.MediaSeason},
                {MediaEpisode, RetrospectorAttribute.MediaEpisode},
                {MediaCategory, RetrospectorAttribute.MediaCategory},
                {MediaDescription, RetrospectorAttribute.MediaDescription},
                {ReviewRating, RetrospectorAttribute.ReviewRating},
                {ReviewUser, RetrospectorAttribute.ReviewUser},
                {ReviewDate, RetrospectorAttribute.ReviewDate},
                {ReviewContent, RetrospectorAttribute.ReviewContent},
                {FactoidTitle, RetrospectorAttribute.FactoidTitle},
                {FactoidContent, RetrospectorAttribute.FactoidContent}
            };
            var comparatorDecoder = new Dictionary<string, Comparator>
            {
                {Equal, Comparator.Equal},
                {GreaterThan, Comparator.GreaterThan},
                {LessThan, Comparator.LessThan},
                {Contains, Comparator.Contains},
            };
            var queryBuilder = new QueryBuilder(
                new LeafOperator(Leaf, commandDecoder, comparatorDecoder),
                new BinaryOperator(OperatorType.And, And),
                new BinaryOperator(OperatorType.Or, Or),
                new UnaryOperator(OperatorType.Not, Not)
            );
            _useCase = new SearchUseCase(_presenter, queryBuilder, dataGateway);
        }

        [Theory]
        [InlineDatas(0, 1, 10)]
        public void empty_search_returns_all_media(int mediaCount)
        {
            _arrangeContext.Media.Add(mediaCount);
            _arrangeContext.SaveChanges();

            Search("");

            Assert.Equal(mediaCount, _presenter.LastResultsPassedTo_Searched.Count());
        }

        [Fact]
        public void default_search_removes_when_media_title_does_match()
        {
            var title = "Best Movie";
            _arrangeContext.Media.Add(new MediaEntity{Title = title});
            _arrangeContext.SaveChanges();

            Search("not "+title);

            Assert.Empty(_presenter.LastResultsPassedTo_Searched);
        }

        [Fact]
        public void default_search_keeps_when_media_title_does_match()
        {
            var title = "Best Movie";
            _arrangeContext.Media.Add(new MediaEntity{Title = title});
            _arrangeContext.SaveChanges();

            Search(title);

            Assert.NotEmpty(_presenter.LastResultsPassedTo_Searched);
        }

        [Fact]
        public void empty_search_gets_all_combinations_of_factoids_and_reviews()
        {
            var factoidCount = 5;
            var reviewCount = 10;
            var mediaId = _arrangeContext.Media.Add(new MediaEntity()).Entity.Id;
            _arrangeContext.Factoids.AddRange(factoidCount, i => new FactoidEntity{MediaId = mediaId});
            _arrangeContext.Reviews.AddRange(reviewCount, i => new ReviewEntity{MediaId = mediaId});
            _arrangeContext.SaveChanges();

            Search("");

            Assert.Equal(factoidCount*reviewCount, _presenter.LastResultsPassedTo_Searched.Count());
        }

        [Theory]
        [InlineData(FactoidTitle, Equal, "Mycroft", "Sherlock", 0)]
        [InlineData(FactoidTitle, Equal, "Sherlock", "Sherlock", 1)]
        [InlineData(FactoidTitle, Equal, "SHERLOCK", "Sherlock", 1)]
        [InlineData(FactoidTitle, Contains, "Sherlock", "Sherlock", 1)]
        [InlineData(FactoidTitle, Contains, "SHERLOCK", "Sherlock", 1)]
        [InlineData(FactoidTitle, Contains, "lock", "Sherlock", 1)]
        [InlineData(FactoidTitle, Contains, "Mycroft", "Sherlock", 0)]
        [InlineData(FactoidTitle, Contains, "MYCROFT", "Sherlock", 0)]
        [InlineData(FactoidTitle, Contains, "croft", "Sherlock", 0)]
        public void search_on_factoid_title_works(string property, string operation, string search, string actual, int numberOfResults)
        {
            var query = $"{Leaf}{property}{operation}{search}";
            _arrangeContext.Media.Add(new MediaEntity
            {
                Factoids = new []{new FactoidEntity
                {
                    Title = actual
                }}
            });
            _arrangeContext.SaveChanges();

            Search(query);

            Assert.Equal(numberOfResults, _presenter.LastResultsPassedTo_Searched.Count());
        }

        [Theory]
        [InlineData(MediaTitle, Equal, "Mycroft", "Sherlock", 0)]
        [InlineData(MediaTitle, Equal, "Sherlock", "Sherlock", 1)]
        [InlineData(MediaTitle, Equal, "SHERLOCK", "Sherlock", 1)]
        [InlineData(MediaTitle, Contains, "Sherlock", "Sherlock", 1)]
        [InlineData(MediaTitle, Contains, "SHERLOCK", "Sherlock", 1)]
        [InlineData(MediaTitle, Contains, "lock", "Sherlock", 1)]
        [InlineData(MediaTitle, Contains, "Mycroft", "Sherlock", 0)]
        [InlineData(MediaTitle, Contains, "MYCROFT", "Sherlock", 0)]
        [InlineData(MediaTitle, Contains, "croft", "Sherlock", 0)]
        public void search_on_media_title_works(string property, string operation, string search, string actual, int numberOfResults)
        {
            var query = $"{Leaf}{property}{operation}{search}";
            _arrangeContext.Media.Add(new MediaEntity
            {
                Title = actual
            });
            _arrangeContext.Media.Add(new MediaEntity
            {
                Title = "Off by One Noise"
            });
            _arrangeContext.SaveChanges();

            Search(query);

            Assert.Equal(numberOfResults, _presenter.LastResultsPassedTo_Searched.Count());
        }

        [Fact]
        public void complex_test_to_verify_everything_works()
        {
            var title = "Sherlock";
            var season = "Sherlock";
            var creator = "BBC";
            var user = "Ben";
            var rating = 5;
            var fact = "Contemporary Fantasy";
            var query = $"`U={user}:`T~{title}|`S~{season}:!`C={creator}:`#>{rating}:`O={fact}";
            _arrangeContext.Media.Add(new MediaEntity
            {
                Title = title,
                Creator = "Not "+creator,
                SeasonId = season,
                Factoids = new []
                {
                  new FactoidEntity{Content = fact},
                  new FactoidEntity()
                },
                Reviews = new []
                {
                    new ReviewEntity {User = user, Rating = rating+1},
                    new ReviewEntity()
                }
            });
            _arrangeContext.Media.Add(5);
            _arrangeContext.SaveChanges();

            Search(query);

            Assert.Single(_presenter.LastResultsPassedTo_Searched);
        }

        private void Search(string query)
        {
            _useCase.Execute(new SearchRequest{Query = query});
        }
    }
}