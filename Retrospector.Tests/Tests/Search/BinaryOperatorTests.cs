using System.Linq;
using Retrospector.Search;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Xunit;

namespace Retrospector.Tests.Tests.Search
{
    public class BinaryOperatorTests
    {
        private IOperator _op;
        private string _syntaxOp;

        public BinaryOperatorTests()
        {
            _syntaxOp = "|";
            _op = new BinaryOperator(OperatorType.And, _syntaxOp);
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
            var query = content;
            var count = 4;
            for (var i = 1; i < count; i++)
                query += $"{_syntaxOp}{content}";

            var pieces = _op.Parse(query).ToList();

            Assert.Equal(count, pieces.Count);
            foreach (var piece in pieces)
                Assert.Equal(content, piece);
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
            var query = $"  {content}  {_syntaxOp}  {content}  ";

            var pieces = _op.Parse(query).ToList();

            foreach (var piece in pieces)
                Assert.Equal(content, piece);
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
            var type = OperatorType.And;

            var op = new BinaryOperator(type, _syntaxOp);

            Assert.Equal(type, op.OperatorType);
        }
    }
}