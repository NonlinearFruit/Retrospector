using System;
using Retrospector.Tests.Utilities;
using Retrospector.Utilities;
using Xunit;

namespace Retrospector.Tests.Tests.Utilities
{
    public class DelegateCommandTests
    {
        public class RaiseCanExecuteChanged : DelegateCommandTests
        {
            [Fact]
            public void does_not_send_event_when_can_execute_predicate_is_null()
            {
                var command = new DelegateCommand(null);
                var countOfEvents = 0;
                command.CanExecuteChanged += (x, y) => countOfEvents++;

                command.RaiseCanExecuteChanged();

                Assert.Equal(Verify.Never, countOfEvents);
            }

            [Fact]
            public void sends_event_when_can_execute_predicate_exists()
            {
                var command = new DelegateCommand(null, (x) => true);
                var countOfEvents = 0;
                command.CanExecuteChanged += (x, y) => countOfEvents++;

                command.RaiseCanExecuteChanged();

                Assert.Equal(Verify.Once, countOfEvents);
            }
        }

        public class CanExecute : DelegateCommandTests
        {
            [Fact]
            public void returns_true_when_can_execute_predicate_is_null()
            {
                var command = new DelegateCommand(null);

                var result = command.CanExecute(null);

                Assert.True(result);
            }

            [Fact]
            public void passes_object_through()
            {
                var obj = Guid.NewGuid();
                var receivedObject = Guid.Empty;
                var command = new DelegateCommand(null, x =>
                {
                    receivedObject = (Guid) x;
                    return true;
                });

                command.CanExecute(obj);

                Assert.Equal(obj, receivedObject);
            }

            [Theory]
            [InlineData(true)]
            [InlineData(false)]
            public void returns_predicate_result_when_predicate_exists(bool predicateResult)
            {
                var command = new DelegateCommand(null, x => predicateResult);

                var result = command.CanExecute(null);

                Assert.Equal(predicateResult, result);
            }
        }

        public class Execute : DelegateCommandTests
        {
            [Fact]
            public void calls_action()
            {
                var countOfActionCalls = 0;
                var command = new DelegateCommand(x => countOfActionCalls++);

                command.Execute(null);

                Assert.Equal(Verify.Once, countOfActionCalls);
            }

            [Fact]
            public void passes_object_through()
            {
                var obj = 1350;
                var objectReceived = 0;
                var command = new DelegateCommand(x => objectReceived = (int) x);

                command.Execute(obj);

                Assert.Equal(obj, objectReceived);
            }
        }
    }
}