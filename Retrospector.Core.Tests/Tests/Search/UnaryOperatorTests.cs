using System.Linq;
using Retrospector.Core.Search;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;
using Xunit;

namespace Retrospector.Core.Tests.Tests.Search
{
    public class UnaryOperatorTests
    {
        private IOperator _op;
        private string _syntaxOp;

        public UnaryOperatorTests()
        {
            _syntaxOp = "|";
            _op = new UnaryOperator(OperatorType.And, _syntaxOp);
        }

        [Theory]
        [InlineData("")]
        [InlineData(" ")]
        [InlineData("\t")]
        [InlineData("\n")]
        public void parses_without_including_empty_pieces(string emptyContent)
        {
            var query = $"{emptyContent}{_syntaxOp}{emptyContent}";

            var pieces = _op.Parse(query).ToList();

            Assert.Empty(pieces);
        }

        [Fact]
        public void parses_correctly()
        {
            var content = "this";
            var query = $"{_syntaxOp}{content}";

            var pieces = _op.Parse(query).ToList();

            Assert.Single(pieces);
            Assert.Equal(content, pieces.First());
        }

        [Fact]
        public void parses_empty_results_when_empty_query()
        {
            var query = "";

            var pieces = _op.Parse(query).ToList();

            Assert.Empty(pieces);
        }

        [Fact]
        public void parses_results_trimming_off_whitespace()
        {
            var content = "word of words";
            var query = $"{_syntaxOp}  {content}  ";

            var pieces = _op.Parse(query).ToList();

            Assert.Equal(content, pieces.First());
        }

        [Fact]
        public void parses_empty_results_when_operator_is_not_at_the_beginning()
        {
            var query = $"this {_syntaxOp} does not contain the {nameof(_syntaxOp)}";

            var pieces = _op.Parse(query).ToList();

            Assert.Empty(pieces);
        }

        [Fact]
        public void parses_empty_results_when_there_is_no_operator_in_the_query()
        {
            var query = $"this does not contain the {nameof(_syntaxOp)}";

            var pieces = _op.Parse(query).ToList();

            Assert.Empty(pieces);
        }

        [Fact]
        public void constructor_sets_operator_type()
        {
            var type = OperatorType.Not;

            var op = new BinaryOperator(type, _syntaxOp);

            Assert.Equal(type, op.OperatorType);
        }
    }
}