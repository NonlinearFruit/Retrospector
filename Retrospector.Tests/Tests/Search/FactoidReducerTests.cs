using Retrospector.DataStorage.Models;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Xunit;

namespace Retrospector.Tests.Tests.Search
{
    public class FactoidReducerTests
    {
        private IFactoidReducer _reducer;

        public FactoidReducerTests()
        {
            _reducer = new FactoidReducer();
        }

        [Theory]
        [InlineData(RetrospectorAttribute.FactoidTitle)]
        [InlineData(RetrospectorAttribute.FactoidContent)]
        public void defaults_to_empty_string(RetrospectorAttribute attribute)
        {
            var result = _reducer.Reduce(null);

            Assert.Equal("", result[attribute]);
        }

        [Theory]
        [InlineData(RetrospectorAttribute.FactoidTitle)]
        [InlineData(RetrospectorAttribute.FactoidContent)]
        public void returns_the_proper_attributes(RetrospectorAttribute attribute)
        {
            var result = _reducer.Reduce(null);

            Assert.Contains(attribute, result.Keys);
        }

        [Fact]
        public void sets_the_title()
        {
            var factoid = new Factoid
            {
                Title = "Genre"
            };

            var result = _reducer.Reduce(factoid);

            Assert.Equal(factoid.Title, result[RetrospectorAttribute.FactoidTitle]);
        }

        [Fact]
        public void sets_the_content()
        {
            var factoid = new Factoid
            {
                Content = "2020"
            };

            var result = _reducer.Reduce(factoid);

            Assert.Equal(factoid.Content, result[RetrospectorAttribute.FactoidContent]);
        }
    }
}