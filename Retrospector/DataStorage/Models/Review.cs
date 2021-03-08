using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Retrospector.DataStorage.Models
{
    [Table("Reviews")]
    public class Review
    {
        [Key]
        public int Id { get; set; }
        public string User { get; set; }
        public int Rating { get; set; }
        public DateTime Date { get; set; }
        public string Content { get; set; }
        public DateTime CreatedDate { get; set; }
        public int MediaId { get; set; }
        public virtual Media Media { get; set; }
    }
}