using System.Collections.Generic;
using Retrospector.Core.Search;
using Retrospector.Core.Search.Models;
using Retrospector.Core.Tests.Utilities;
using Xunit;

namespace Retrospector.Core.Tests.Tests.Search
{
    public class LeafOperatorTests
    {
        private LeafOperator _op;
        private string _syntaxOp;
        private string _command;
        private string _comparator;
        private Dictionary<string, RetrospectorAttribute> _commandDecoder;
        private Dictionary<string, Comparator> _comparatorDecoder;

        public LeafOperatorTests()
        {
            _syntaxOp = "|";
            _command = "U";
            _comparator = "~";
            _commandDecoder = new Dictionary<string, RetrospectorAttribute>
            {
                {_command, RetrospectorAttribute.ReviewUser}
            };
            _comparatorDecoder = new Dictionary<string, Comparator>
            {
                {_comparator, Comparator.Contains}
            };
            _op = new LeafOperator(_syntaxOp, _commandDecoder, _comparatorDecoder);
        }

        [Theory]
        [InlineData(" ")]
        [InlineData("\t")]
        [InlineData("\n")]
        public void parses_some_when_trimming_off_whitespace(string whitespace)
        {
            var content = "word of words";
            var query = $"{whitespace}{_syntaxOp}{whitespace}{_command}{whitespace}{_comparator}{whitespace}{content}{whitespace}";

            var option = _op.Parse(query);

            Verify.Some(option);
        }

        [Fact]
        public void parses_correctly()
        {
            var content = "this";
            var query = $"{_syntaxOp}{_command}{_comparator}{content}";

            var option = _op.Parse(query);

            Verify.Some(option);
            Assert.True(option.Exists(leaf => leaf.Attribute == _commandDecoder[_command]));
            Assert.True(option.Exists(leaf => leaf.Comparator == _comparatorDecoder[_comparator]));
            Assert.True(option.Exists(leaf => leaf.SearchValue == content));
        }

        [Fact]
        public void parses_none_when_no_command()
        {
            var content = "this";
            var query = $"{_syntaxOp}{_comparator}{content}";

            var option = _op.Parse(query);

            Verify.None(option);
        }

        [Fact]
        public void parses_none_when_no_comparator()
        {
            var content = "this";
            var query = $"{_syntaxOp}{_command}{content}";

            var option = _op.Parse(query);

            Verify.None(option);
        }

        [Fact]
        public void parses_none_when_no_content()
        {
            var query = $"{_syntaxOp}{_command}{_comparator}";

            var option = _op.Parse(query);

            Verify.None(option);
        }

        [Fact]
        public void parses_none_when_bad_operator()
        {
            var content = "this";
            var query = $"@{_command}{_comparator}{content}";

            var option = _op.Parse(query);

            Verify.None(option);
        }

        [Fact]
        public void parses_none_when_bad_command()
        {
            var content = "this";
            var query = $"{_syntaxOp}@{_comparator}{content}";

            var option = _op.Parse(query);

            Verify.None(option);
        }

        [Fact]
        public void parses_none_when_bad_comparator()
        {
            var content = "this";
            var query = $"{_syntaxOp}{_command}@{content}";

            var option = _op.Parse(query);

            Verify.None(option);
        }

        [Fact]
        public void parses_none_when_empty_query()
        {
            var query = "";

            var option = _op.Parse(query);

            Verify.None(option);
        }

        [Fact]
        public void parses_none_when_operator_is_not_at_the_beginning()
        {
            var query = $"this {_syntaxOp} does contain the {nameof(_syntaxOp)}";

            var option = _op.Parse(query);

            Verify.None(option);
        }
    }
}